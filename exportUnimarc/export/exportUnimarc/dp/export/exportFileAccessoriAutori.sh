#java -jar sqlworkbench.jar -script=export.sql -profile='localhost'

ora="date +%Y-%m-%d-%H:%M:%S"
echo `${ora}` "Inizio processo di exportFileAccessoriAutori.sh" > exportFileAccessoriAutori.proc
  

java -Xmx1560m -classpath ../:postgresql-8.2-509.jdbc2.jar it.finsiel.offlineExport.DbDownload downloadFileAccessoriAutoriXXX.txt

echo `${ora}` "Fine processo di exportFileAccessoriAutori.sh" >> exportFileAccessoriAutori.proc

  
