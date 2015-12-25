package groovy

import org.ametiste.utility.xmas.domain.model.TransactionSupport
import org.ametiste.utility.xmas.infrastructure.AbstractRelayConfigurationFactory
import org.ametiste.utility.xmas.infrastructure.converter.MessageConverter
import org.ametiste.utility.xmas.infrastructure.converter.ModifyConverter
import org.ametiste.utility.xmas.infrastructure.converter.model.AddParamPair
import org.ametiste.utility.xmas.infrastructure.converter.model.ModifyCondition
import org.ametiste.utility.xmas.infrastructure.converter.model.ParamPair
import org.ametiste.utility.xmas.infrastructure.converter.model.ParamValue
import org.ametiste.utility.xmas.infrastructure.transaction.TransactionException
import org.ametiste.utility.xmas.infrastructure.transaction.TransactionStrategy
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate

class GroovyRelayConfigurationFactory extends AbstractRelayConfigurationFactory {

    GroovyRelayConfigurationFactory() {
//        addBridgeConfiguration("http://10.5.4.134:8081", TransactionSupport.ENABLED); //adds bridge configuration with transaction support, with logging enabled by default
//        addBridgeConfiguration("http://10.5.4.134:8081", RelayLogging.DISABLED); //adds bridge configuration with by default disabled transaction support, with logging disabled
//        addBridgeConfiguration("http://10.5.4.134:8081", RelayLogging.DISABLED, TransactionSupport.ENABLED); //adds bridge configuration with both defaults changed, enabled transaction support, with logging disabled
//        addTransformConfiguration(new DphStringMessageConverter(), "http://10.5.4.134:8081"); //adds configuration with defined converter that converts incoming message data to string  with disabled transaction support and logging enabled.
//        addCustomConfiguration(new DummyRelayConfiguration()); // adds custom relay configuration

        def map = new HashMap<Object, Double>()

        map.put("similar-bestmatch", 0.5);
        map.put("similar-newestfirst", 0.3);
        map.put("similar-undiscovered", 0.5);
        map.put("same-series-bestmatch", 0.1);

        addParametriziedConfiguration(new DphElasticScanMessageConverter(),
                new PostContentTypeBasedTransactionStrategy<Map<String, Object>>(new RestTemplate(),
                        "http://rain.hlss.depositphotos.com/scan?provider=elastic-scan",
                        new MediaType("application", "vnd.dph.hlss.request-ld+json", new Collections.SingletonMap<String, String>("profile", "query")),
                        new MediaType("application", "vnd.dph.hlss.miditems-brief-list+json")

                ));

//
//
//        addGETMethodDataConfiguration("http://206.54.167.81/query",
//                new ModifyConverter(Arrays.asList(
//                        new AddParamPair("sort", new ConstantParamValue("similar-bestmatch"), ModifyCondition.STRICT),
//                        new RenameParameterPair("id", "source_item_id", ModifyCondition.STRICT))
//                ),
//                TransactionSupport.ENABLED)

        addGETMethodDataConfiguration("http://206.54.167.81/query",
                new ModifyConverter(Arrays.asList(
                        new AddParamPair("sort", new RandomSortWithWeightParamValue(map), ModifyCondition.STRICT),
                        new RenameParameterPair("id", "source_item_id", ModifyCondition.STRICT))
                ),
                TransactionSupport.ENABLED)
    }
}

class RandomSortWithWeightParamValue implements ParamValue {

    private Map<Object, Double> weightedValues
    private Double sum = 0;

    public RandomSortWithWeightParamValue(Map<Object, Double> weightedValues) {

        this.weightedValues = weightedValues
        for(Map.Entry<Object, Double> entry: weightedValues.entrySet()) {
            sum = sum + entry.getValue();
        }
    }

    @Override
    Object getValue() {

        def limit = new Random().nextDouble() * sum;
        def reduced = limit;
        for(Map.Entry<Object, Double> entry: weightedValues.entrySet()) {
            reduced = reduced - entry.getValue();
            if(reduced <=0) {
                System.out.println("limit: " + limit );
                System.out.println("return sort: " + entry.getKey() );
                return  entry.getKey();
            }
        }
        return null;// hopefully cant happen
    }
}

class RenameParameterPair implements ParamPair {

    private String from
    private String to
    private ModifyCondition condition

    public RenameParameterPair(String from, String to, ModifyCondition condition) {
        this.from = from
        this.to = to
        this.condition = condition

    }

    @Override
    void applyTo(Map<String, Object> filledData) {
        if (filledData.containsKey(from)) {
            filledData.put(to, filledData.get(from));
            filledData.remove(from);
        } else {
            if (condition.equals(ModifyCondition.STRICT)) {
                throw new IllegalArgumentException("Request should have parameter  " + from + " for renaming, however it doesnt")
            }
        }

    }
}

class DphElasticScanMessageConverter implements MessageConverter<Map<String, Object>> {
    @Override
    boolean isDataSupported(Map<String, Object> data) {
        return data.containsKey("sort") && data.containsKey("q") && data.containsKey("from") && data.containsKey("len")
    }

    @Override
    Map<String, Object> convert(Map<String, Object> data) {

        def terms = new ArrayList<Map<String, String>>();
        String query = data.get("q")

        def keywords = query.split(" ");
        for (String keyword : keywords) {
            def keywordTerm = new HashMap<String, String>()
            keywordTerm.put("@type", "term");
            keywordTerm.put("match", keyword);
            terms.add(keywordTerm);
        }

        def condition = new HashMap();
        condition.put("@type", "logical");
        condition.put("or", terms);


        def queryTerm = new HashMap<String, Object>();
        queryTerm.put("@type", "query");
        queryTerm.put("policy", data.get("sort"));
        queryTerm.put("condition", condition);

        def paginationTerm = new HashMap();
        paginationTerm.put("@type", "pagination");
        paginationTerm.put("limit", Integer.parseInt(data.get("len")))
        paginationTerm.put("offset", Integer.parseInt(data.get("from")));

        def constraints = new ArrayList();
        constraints.add(paginationTerm);
        constraints.add(queryTerm);

        def map = new HashMap<String, Object>();
        map.put("@type", "request");
        map.put("constraints", constraints);
        return map;
    }
}


class PostContentTypeBasedTransactionStrategy<T> implements TransactionStrategy<T> {

    private final RestTemplate restTemplate;
    private final String uri;
    private MediaType contentType;
    private MediaType acceptType

    public PostContentTypeBasedTransactionStrategy(RestTemplate template, String uri, MediaType contentType, MediaType acceptType) {
        this.acceptType = acceptType
        this.contentType = contentType;
        this.restTemplate = template;
        this.uri = uri;
    }

    public void process(T typedData) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(contentType);
            headers.setAccept(Arrays.asList(acceptType))

            HttpEntity<T> request = new HttpEntity<T>(typedData, headers);
            restTemplate.exchange(uri, HttpMethod.POST, request, Void.class);
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }

    @Override
    public void processFailure(T typedData) {

    }
}
