package groovy.apriori

import java.lang.reflect.Array

/**
 * Created by aisma on 18.05.2016.
 */
class CSVReader {
    def path
    def headers;
    ArrayList<Array> body=new ArrayList<>()
    def read(){
        new File(path).getText('UTF-8').eachLine { String line,lineIndex->
            if(lineIndex) {
                lineIndex++
                body[lineIndex-1]=line.split(';').drop(1)

            }else{
                lineIndex++
                headers=line.tokenize(';').drop(1)

            }
        }
        return [path,headers,body]
    }
}

