curl -XPUT 'localhost:9200/_river/my_river_jdbc/_meta' -d '{
    "strategy":"simple",
    "type": "jdbc",
    "flush_interval":20s
    "jdbc": {
        "url": "jdbc:mysql://localhost:3306/auto",
        "user": "root",
        "password": "",
        "sql" : [
                    {
                        "statement" : " SELECT d.`id` AS _id,d.`image`,  CONCAT(d.`latitude`,\",\",d.`longitude`) AS location,d.`detail`,d.`createAt`,d.`category`,d.`type`,d.`reward`,d.`discoveryTime`,d.`discoveryAddress`,d.`image`,d.`infoAddress`,d.`favoriteCount`,d.`replyCount`,d.`status`,d.`adoptAnswer`,d.`userId` AS `user.id`,u.`nickName` AS `user.nickName`,u.`sex` AS `user.sex`,u.`img`  AS `user.img`  FROM discovery d,`user` u WHERE u.`id`=d.`userId`  and d.`createAt` > ?",
                        "parameter" : [ "$river.state.last_active_begin" ]
                    }
                ],
    "index" : "test",
    "type" : "discovery"
    }

}'