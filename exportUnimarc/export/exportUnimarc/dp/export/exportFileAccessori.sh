#java -jar sqlworkbench.jar -script=export.sql -profile='localhost'

ora="date +%Y-%m-%d-%H:%M:%S"
echo `${ora}` "Inizio processo di exportFileAccessori.sh" > exportFileAccessori.proc
  

java -Xmx1560m -classpath ../:postgresql-8.2-509.jdbc2.jar it.finsiel.offlineExport.DbDownload downloadFileAccessoriXXX.txt

echo `${ora}` "Fine processo di exportFileAccessori.sh" >> exportFileAccessori.proc

  
