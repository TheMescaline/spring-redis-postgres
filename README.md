# spring-redis-postgres

###Start environment:

#####**Single Redis server**
1. раскоментировать в файле "RedisCacheConfig.java" строку 72
2. закоментировать в файле "RedisCacheConfig.java" строки 51-55, 75-78
3. в файле "application.properties" закомментировать настройки для кластера (строки 14, 32-33)  
4. в файле "application.properties" раскомментировать настройки для одиночного сервера (строки 15, 25-28)  
5. настроить окружение (см. ENV):  
`cd ENV`  
`docker-compose -f docker-compose-single.yml up -d ` 
6. запустить приложение и использовать API (см. ниже)
7. даем команды API и наблюдаем лог приложения

_**start redis client:**_  
Подключение к redis серверу:  
`docker run -it --network redisnet --rm redis redis-cli -h redis-single`  
Команды для работы с redis сервером ниже.

Описание:
При запросе поста с id 1 или 3, первый запрос из базы длится около 3с. Запрос кешируется.
Следующие запросы отдаются из кеша менее 1с.
Кешируются только посты с id 1 или 3, т.к. соответствуют условию (shares > 500). Остальные посты не кешируются.
Затем останавливаем redis сервер.  
`docker stop redis-single`  
При запросе поста с id 1 или 3, информация получется из базы, длится около 3с.
При восстановлении сервера информация берется из кеша. 

#
#####**Cluster Redis server**  
1. закоментировать в файле "RedisCacheConfig.java" строку 72
2. раскоментировать в файле "RedisCacheConfig.java" строки 51-55, 75-78
3. в файле "application.properties" раскомментировать настройки для кластера (строки 14, 32-33)
4. в файле "application.properties" закомментировать настройки для одиночного сервера (строки 15, 25-28) 
5. настроить окружение (нужно, чтобы приложение запускалось в одной сети с кластером и собираем jar):  
`build clean bootJar`  
`cd ENV`  
`docker-compose -f docker-compose-cluster.yml up -d`  
8. для наблюдения за логом приложения используем:  
`docker logs redis-webapp`
  
**_start redis client:_**  
Подключение к redis серверу:  
`docker run -it --network redisnet --rm redis redis-cli -h redis-1 -p 7001`  
Команды для работы с redis сервером ниже.  

#
###API
#####Get top posts:
**GET** _[http://localhost:8080/posts/top](http://localhost:8080/posts/top)_

#####Get post by id:
Posts with id 1 and 3 are cached because "shares" > 500  
**GET** _[http://localhost:8080/posts/1/](http://localhost:8080/posts/1)_

#####Update post:
**PUT** _[http://localhost:8080/posts/update](http://localhost:8080/posts/update)_

body:  
{"id": 1, "title": "*****","description": "#####", "image": "@@@none@@@","shares": 600,"author":{"name":"Anna"}}

#####Delete post by id from cache "post-single":
**DELETE** _[http://localhost:8080/posts/delete/1/](http://localhost:8080/posts/delete/1)_

#####Clear cache "post-top":
**DELETE** _[http://localhost:8080/posts/top/evict](http://localhost:8080/posts/top/evict)_

#
_redis benchmark:_  
redis-benchmark -q -n 100000 -c 50 -P 12  
redis-benchmark -c 60000 -q -n 10 -t get,set
#
_redis log:_  
tail -f /var/log/redis/redis-server.log

#
_redis-cli command:_  
127.0.0.1:6379>  
- AUTH password
- INFO (persistence, cluster, replication, cpu, memory, server, stats, replication, ...)
- TTL "single-post"
- MONITOR (end monitor Ctrl-C)
- CLUSTER NODES  
- CLUSTER FAILOVER [FORCE|TAKEOVER]  
- CLUSTER COUNT-FAILURE-REPORTS node-id
- DEBUG SEGFAULT
- FLUSHALL


