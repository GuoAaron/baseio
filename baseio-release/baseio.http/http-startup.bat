@rem >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
java -Xms128m -Xmx512m -verbose:gc -Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -cp .;.\lib\*; com.generallycloud.nio.container.http11.startup.HttpServerStartup