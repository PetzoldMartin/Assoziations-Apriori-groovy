package groovy.apriori

/**
 * Created by aisma on 18.05.2016.
 */

    class Main {
        static void main(String... args) {
           makeout(6);
            makeout(10);
            makeout(20);
            makeout(30);


        }

        static def makeout(minsupp){
            def minSupport=minsupp
            println 'test'
            def pos=new Apriori(csvReader:new CSVReader(path: 'res/test.csv').read(), minSupportRel: minSupport)
            pos.makeRules()
            pos.printAndAnalyze()
            println 'Negativ'
            def pos2=new Apriori(csvReader:new CSVReader(path: 'res/negativ.csv').read(), minSupportRel: minSupport)
            pos2.makeRules()
            pos2.printAndAnalyze()
            println 'Positive'
            def pos3= new Apriori(csvReader:new CSVReader(path: 'res/positiv.csv').read(), minSupportRel: minSupport)
            pos3.makeRules()
            pos3.printAndAnalyze()
            println 'Both'
            def csb=new CSVReader(path: 'res/positiv.csv').read()
            csb.setPath('res/negativ.csv')
            csb.read()
            csb.setPath('res/both.csv')
            def pos4 =new  Apriori(csvReader:csb, minSupportRel: minSupport)
            pos4.makeRules()
            pos4.printAndAnalyze()
        }
    }

