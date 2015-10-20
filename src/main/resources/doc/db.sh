  curl -XPUT 'http://localhost:9200/dis/' -d '
    {
       "settings": {
           "number_of_shards": 1,
           "number_of_replicas": 2
       },
        "mappings": {
            "discovery": {
                "properties": {
                    "detail": {
                        "type": "string",
                        "store": "yes",
                        "index": "analyzed",
                        "analyzer": "ik"
                    },
                    "category": {
                        "type": "string",
                        "store": "yes",
                        "index": "not_analyzed"
                    },
                    "type": {
                        "type": "integer",
                        "store": "yes",
                        "index": "not_analyzed"
                    },
                    "reward": {
                        "type": "integer",
                        "store": "yes",
                        "index": "not_analyzed"
                    },
                    "discoveryTime": {
                        "type": "long",
                        "store": "yes",
                        "index": "not_analyzed"
                    },
                    "discoveryAddress": {
                        "type": "string",
                        "store": "yes",
                        "index": "no"
                    },
                    "location": {
                        "type": "geo_point"
                    },
                    "image": {
                        "type": "string",
                        "store": "yes",
                        "index": "no"
                    },
                    "infoAddress": {
                        "type": "string",
                        "store": "yes",
                        "index": "no"
                    },
                    "favoriteCount": {
                        "type": "long",
                        "store": "yes",
                        "index": "not_analyzed"
                    },
                    "replyCount": {
                        "type": "long",
                        "store": "yes",
                        "index": "not_analyzed"
                    },
                    "status": {
                        "type": "integer",
                        "store": "yes",
                        "index": "no"
                    },
                    "adoptAnswer": {
                        "type": "integer",
                        "store": "yes",
                        "index": "no"
                    },
                    "createAt": {
                        "type": "long",
                        "store": "yes",
                        "index": "not_analyzed"
                    },
                    "user": {
                        "type": "object",
                        "properties": {
                            "id": {
                                "type": "integer",
                                "store": "yes",
                                "index": "no"
                            },
                            "nickName": {
                                "type": "string",
                                "store": "yes",
                                "index": "no"
                            },
                            "img": {
                                "type": "string",
                                "store": "yes",
                                "index": "no"
                            },
                            "sex": {
                                "type": "integer",
                                "store": "yes",
                                "index": "no"
                            }
                        }
                    }
                }
            }
        }
    }'

    curl -XPOST 'http://localhost:9200/_aliases' -d '
          {
              "actions" : [
                  { "add" : { "index" : “test", "alias" : "discoveries" } }
              ]
          }'