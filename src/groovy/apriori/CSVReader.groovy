package groovy.apriori

import java.lang.reflect.Array

/**
 * Created by aisma on 18.05.2016.
 */
class CSVReader {
    String path
    def headers=[]
    def body=[[]]
    def read(){
        new File(path).getText('UTF-8').eachLine { String line,lineIndex->
            line=line+";1"
            if(lineIndex) {

                def x=(line.split(';').collect{if(it)(((it.replaceAll(",","\\.")) as double)*100)as int}.drop(1).dropRight(1))
                if(body[lineIndex-1])body[lineIndex-1]+=x else body[lineIndex-1]=x
                lineIndex++
                //println body[lineIndex-1].size()+":::"+  body[lineIndex-1]
            }else{
                lineIndex++
                headers+=line.tokenize(';').drop(1).dropRight(1)

            }
        }

        return this;
    }

    def clear(){
        path=''
        headers=[]
        body=new ArrayList<>();
    }
}

