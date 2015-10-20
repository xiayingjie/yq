cd /opt/
wget  http://download.elastic.co/elasticsearch/elasticsearch/elasticsearch-1.7.2.zip
unzip elasticsearch-1.7.2.zip
#初始化环境变量
echo '
export ES_HOME=/opt/elasticsearch-1.7.2
export PATH=$ES_HOME/bin:$PATH
export CLASSPATH=$ES_HOME/lib:$CLASSPATH' >> /etc/profile

source /etc/profile


#下载相应的jar包
wget -P $ES_HOME/lib/ http://central.maven.org/maven2/org/apache/httpcomponents/httpclient/4.3.5/httpclient-4.3.5.jar
wget -P $ES_HOME/lib/ http://central.maven.org/maven2/org/apache/httpcomponents/httpcore/4.3.3/httpcore-4.3.3.jar
#安装ik
wget -P $ES_HOME/myplugin https://github.com/medcl/elasticsearch-analysis-ik/archive/master.zip
cd $ES_HOME/myplugin/
unzip $ES_HOME/myplugin/master.zip
cd  $ES_HOME/myplugin/elasticsearch-analysis-ik-master/
mvn package
cp target/elasticsearch-analysis-ik-1.4.0.jar  $ES_HOME/lib/
mv $ES_HOME/myplugin/elasticsearch-analysis-ik-master/config/ik  $ES_HOME/config/
#添加通用分词器
echo
'
index:
  analysis:
    analyzer:
      ik:
          alias: [ik_analyzer]
          type: org.elasticsearch.index.analysis.IkAnalyzerProvider
      ik_max_word:
          type: ik
          use_smart: false
      ik_smart:
          type: ik
          use_smart: true
index.analysis.analyzer.default.type: ik
' >>$ES_HOME/config/elasticsearch.yml


#安装mysql_river-jdbc数据同步  es安装目录
$ES_HOME/bin/plugin --install jdbc --url http://xbib.org/repository/org/xbib/elasticsearch/plugin/elasticsearch-river-jdbc/1.5.0.5/elasticsearch-river-jdbc-1.5.0.5-plugin.zip

wget -P $ES_HOME/plugins/jdbc/ http://central.maven.org/maven2/mysql/mysql-connector-java/5.1.36/mysql-connector-java-5.1.36.jar
#初始化river-jdbc插件 数据
curl -XPUT 'localhost:9200/_river/my_river_jdbc/_meta' -d '{
    "strategy":"simple",
    "type": "jdbc",
    "jdbc": {
        "url": "jdbc:mysql://localhost:3306/auto",
        "user": "root",
        "password": "",
        "sql": "SELECT d.`id` AS _id,d.`image`,  CONCAT(d.`latitude`,\",\",d.`longitude`) AS location,d.`detail`,d.`createAt`,d.`category`,d.`type`,d.`reward`,d.`discoveryTime`,d.`discoveryAddress`,d.`image`,d.`infoAddress`,d.`favoriteCount`,d.`replyCount`,d.`status`,d.`adoptAnswer`,d.`userId` AS `user.id`,u.`nickName` AS `user.nickName`,u.`sex` AS `user.sex`,u.`img`  AS `user.img`  FROM discovery d,`user` u WHERE u.`id`=d.`userId`",
    "index" : "test",
    "type" : "discovery"
    }

}'