package groovy.apriori

import java.lang.reflect.Array

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

        itemSet.each { List setLine ->
            def r=[]
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
            println 'hc: '+ hitcounter
            println  'sum: '+ r.sum()
            setLine.each {Item lineItem->
                lineItem.count=r.sum()
            }


        }

    }

    protected def makeRules(def loop) {

        def loopI = loop
        println 'LOOP' + loop
        if (loopI == 0) {
            //make first candidates
            def itemSet = prepareData()
            countCandidates(itemSet)
            candidateSets.add(loop, itemSet.clone())
            itemSet.removeIf { ((Item) it[0]).count < getMinSupportAbs() }
            largeItemSets.add(loop, itemSet.clone())
            debug(loop)
            makeRules(loopI + 1)
        } else {
            if (candidateSets.size() >= loop) {
                def itemSet = []
                itemSet = combinationsOf(prepareCandidateBuild(largeItemSets[loop - 1] as List), loop + 1)
                if (itemSet) {
                    countCandidates(itemSet)
                    candidateSets.add(loop, itemSet.clone())
                    //itemSet.removeIf {lookAfterCandidateMinsupp(it)}
                    largeItemSets.add(loop, itemSet.clone())
                    debug(loop)
                    makeRules(loopI + 1)
                }
            }
            debug(0)

        }
    }


    List prepareCandidateBuild(List list) {
        def newList = []
        list.each {
            it.each { item ->
                if (!newList.contains(item)) newList.add(item.clone())
            }
        }
        return newList
    }

    def debug(def loop) {
        candidateSets[loop].each {
            println([it.id, it.count,it],)
        }
        println '-------------------'
//        largeItemSets[loop].each {
//            println([it.id, it.count])
//        }
    }

    List combinationsOf(List list, int r) {
        if ((0..<list.size() + 1).contains(r))// validate input
        {
            def combs = [] as Set
            list.eachPermutation {
                combs << it.subList(0, r).sort {
                    a, b -> a <=> b
                }
            }
            combs as List
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
        return this.id <=> o.id;
    }

    @Override
    boolean equals(Object obj) {
        return this.id == obj.id && this.name == obj.name
    }

}
