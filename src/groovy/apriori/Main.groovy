package groovy.apriori

/**
 * Created by aisma on 18.05.2016.
 */

    class Main {
        static void main(String... args) {
            def minSupport=20;

            println 'Negativ'
            def pos=new Apriori(csvReader:new CSVReader(path: 'res/negativ.csv').read(), minSupportRel: minSupport)
            pos.makeRules()
//            println 'Positive'
//            new Apriori(csvReader:new CSVReader(path: 'res/positiv.csv').read(), minSupportRel: minSupport)
//            println 'Both'
//            def csb=new CSVReader(path: 'res/positiv.csv').read()
//            csb.setPath('res/negativ.csv')
//            csb.read()
//            new Apriori(csvReader:csb, minSupportRel: minSupport)
        }
    }

