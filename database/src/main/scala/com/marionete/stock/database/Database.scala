package com.marionete.stock.database

import org.neo4j.driver.v1.{AuthTokens, GraphDatabase}

/**
  * Created by matheussilveira on 05/08/2016.
  */
class Database {
  val driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "neo4j" ) )
  val session = driver.session()

  session.run( "CREATE (a:Person {name:'Arthur', title:'King'})" )

  val result = session.run( "MATCH (a:Person) WHERE a.name = 'Arthur' RETURN a.name AS name, a.title AS title" );
  while ( result.hasNext() )
  {
    val record = result.next()
    println( record.get( "title" ).asString() + " " + record.get("name").asString() )
  }

  session.close()
  driver.close()
}
