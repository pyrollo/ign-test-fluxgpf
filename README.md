# Test Geotools pour la migration des flux geopf

```
mvn package 
java -cp target/geotools-1.0-SNAPSHOT-jar-with-dependencies.jar fr.ignfab.geotools.App
```
mvn compile exec:java -Dexec.mainClass="fr.ignfab.geotools.App"
--> ça se lance mais pb de proxy à résoudre
mvn compile exec:java -Dexec.mainClass="fr.ignfab.geotools.App" \
 -Dhttp.proxyHost=proxy.ign.fr -Dhttp.proxyPort=3128 -Dhttps.proxyHost=proxy.ign.fr -Dhttps.proxyPort=3128 


