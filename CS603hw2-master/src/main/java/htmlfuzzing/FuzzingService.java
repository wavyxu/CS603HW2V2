package htmlfuzzing;
import htmlfuzzing.spi.Fuzzer;

import java.util.*;

public class FuzzingService {
    private static FuzzingService service;
    private ServiceLoader<Fuzzer> loader;
    private List<Fuzzer> services;

    private FuzzingService() {

        loader = ServiceLoader.load(Fuzzer.class);
        services = new ArrayList<>();
        loadServices();

    }

    private void loadServices() {
        Iterator<Fuzzer> iter = loader.iterator();
        while (iter.hasNext()) {
            services.add(iter);
        }
    }

    public static synchronized FuzzingService getInstance() {
        if (service == null) {
            service = new FuzzingService();
        }
        return service;
    }

    private Fuzzer getTagRemover(){
        return services.get(0);
    }

    private Fuzzer getTagReplacer(){
        return services.get(1);
    }

    private Fuzzer getTagInserter(){
        return services.get(2);
    }

    public void singleModification(String htmlstr){
        Random idx = new Random();

        if ((idx.nextInt(1000) & 1) == 0) {
            Fuzzer fuzzer  = getTagRemover();
            fuzzer.fuzz(htmlstr);
        } else {
            Fuzzer fuzzer = getTagReplacer();
            fuzzer.fuzz(htmlstr);
        }
    }

    public void combinedModification(String htmlstr){
        Random count = new Random();
        for (int i = 0; i < count.nextInt(10); i++) {
            Fuzzer fuzzer = getTagReplacer();
            fuzzer.fuzz(htmlstr);
        }

    }

    public void insertHTML(String htmlstr){
        getTagInserter().fuzz(htmlstr);
    }
}
