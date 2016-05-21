package groovy.apriori

import org.paukov.combinatorics.Factory
import org.paukov.combinatorics.Generator
import org.paukov.combinatorics.ICombinatoricsVector


/**
 * Created by aisma on 18.05.2016.
 */
class Apriori {
    CSVReader csvReader;
    def library = []
    def candidateSets = []
    def largeItemSets = []
    def minSupportRel

    def getMinSupportAbs() {
        csvReader.body.size() * 10 * minSupportRel / 100
    }

    def prepareData() {
        def itemSet = []
        csvReader.headers.eachWithIndex { entry, int i ->
            itemSet.add([new Item(name: entry, id: i, count: 0)])
        }
        csvReader.body.each { line ->
            def libLine = []
            line.eachWithIndex { it, int i ->
                if (it) libLine.add((Item) (itemSet.get(i)[0].clone()).setCount(it))
            }
            library.add(libLine)
        }
        return itemSet
    }

    def countCandidates(List itemSet) {
        //backset is for independence the elements
        List backSet = []
        itemSet.each { List setLine ->
            List backSetLine = []
            def r = []
            def hitcounter = 0
            library.each {
                List libLine ->
                    def count = []
                    if (libLine.containsAll(setLine)) {
                        hitcounter += 1
                        libLine.each {
                            Item libLineElement ->
                                if (setLine.contains(libLineElement)) {
                                    count.add(((Item) libLineElement).count)
                                }
                        }
                        r.add(count.min())
                    }

            }
//            println 'hc: '+ hitcounter
//            println  'sum: '+ r.sum()
            setLine.each { Item lineItem ->
                lineItem.count = r.sum()
                backSetLine.add(lineItem.clone())
            }
            backSet.add(backSetLine)

        }
        return backSet
    }

    protected def makeRules(def loop) {

        def loopI = loop
        println 'LOOP' + loop
        if (loopI == 0) {
            def itemSet = prepareData()
            itemSet = countCandidates(itemSet)
            candidateSets.add(loop, itemSet.clone())
            itemSet.removeIf { ((Item) it[0]).count < getMinSupportAbs() }
            largeItemSets.add(loop, itemSet.clone())
            debug(loop)
            makeRules(loopI + 1)
        } else {
            if (candidateSets.size() >= loop) {
                def itemSet = []
                // Create the initial vector
                ICombinatoricsVector<Item> initialVector = Factory.createVector(prepareCandidateBuild(largeItemSets[loop - 1] as List));
                // Create a simple combination generator to generate loop+1-combinations of the initial vector
                Generator<Item>  gen = Factory.createSimpleCombinationGenerator(initialVector, loop+1);
                // Print all possible combinations
                gen.each {
                    itemSet.add(it.getVector());
                }
                if (itemSet) {
                    itemSet = countCandidates(itemSet)
                    candidateSets.add(loop, itemSet.clone())
                    itemSet.removeIf { ((Item) it[0]).count < getMinSupportAbs() }
                    largeItemSets.add(loop, itemSet.clone())
                    debug(loop)
                    makeRules(loopI + 1)
                }
            }
            //debug(0)

        }
    }


    List prepareCandidateBuild(List list) {
        def newList = []
        list.each {
            it.each { item ->
                if (!newList.contains(item)) newList.add(item)
            }
        }
        return newList
    }

    def debug(def loop) {
//        candidateSets[loop].each {
//            println([it.id, it.count,it],)
//        }
        println '-------------------'
        largeItemSets[loop].each {
            println([it.id, it.count, it])
        }
    }


    def makeRules() {
        makeRules(0)
    }
}


class Item implements Cloneable, Comparable {
    def name, count
    int id = 0;

    def setCount(def count) {
        this.count = count;
        return this;
    }

    def add(int i) {
        this.count = this.count + i;
    }

    @Override
    int compareTo(Object o) {
        if (o instanceof Item) {
            return this.id <=> o.id;
        } else return 0;
    }

    @Override
    boolean equals(Object obj) {
        if (obj instanceof Item) {
            return this.id == obj.id
        } else return false
    }

    @Override
    Item clone() {
        return new Item(count: this.count, name: this.name, id: this.id)
    }

}
