package groovy.apriori

/**
 * Created by aisma on 18.05.2016.
 */

    class Main {
        static void main(String... args) {
            println 'Groovy world!'
            new CSVReader(path: 'res/negativ.csv').read()

        }
    }

