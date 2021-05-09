Typically, a microservice will have a single data source for its data. This additional Lothric data source is 
intended for services to use when they are migrating legacy PHP code to new Java microservices, and they need 
to directly access the lothric database during the transition. Delete this `lothric` package if your service 
does not interact with the lothric database directly. Otherwise, add models and repositories for only those 
entities that you need to support your migration.

_NOTE: If you delete this package you may also delete `starterservice/config/DataSourceConfig.java`.