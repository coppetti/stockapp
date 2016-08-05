

val lisbuf:scala.collection.mutable.Map[String,Tuple2[String,String]] =  scala.collection.mutable.Map()

lisbuf.put("Yahoo",("120","120"))
println(lisbuf)
if(lisbuf.contains("Yahoo")) lisbuf("Yahoo")=("110","09")
println(lisbuf("Yahoo")._2)