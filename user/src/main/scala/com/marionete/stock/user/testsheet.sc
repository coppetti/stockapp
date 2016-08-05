def extractDouble(str:String): Double={
  str.split(" ").map(s  => if(s.replace(".","").forall(_.isDigit) ) s ).last.toString.toDouble
}

extractDouble("jshdka ajkhdkj akjhsdka 123.09")