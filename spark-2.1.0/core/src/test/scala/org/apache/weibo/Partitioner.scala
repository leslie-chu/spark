package org.apache.weibo

//import org.apache.spark.rdd.RDD

import org.apache.weibo.rdd.RDD
import org.apache.weibo.util.Utils
//import org.apache.weibo.Partitioner
/**
  * 一个对象定义了元素怎样以key-value pair RDD分区的
  * 映射key和partitionID,from 0 to numPartition-1
  */

 abstract  class Partitioner extends Serializable {
  def numPartition(): Int
  def getPartition(key: Any): Int
}

private[weibo] object Partitioner{

  def Partitioner(rdd:RDD[_],others:RDD[_]*): Partitioner={
    val rdds = (Seq(rdd)++others)
    val hasPartitioner = rdds.filter(_.partitioner.exists(_.numPartitions>0))
    if(hasPartitioner.nonEmpty){
      hasPartitioner.maxBy(_.partitions.length).partitioner.get
    }else{
      if (rdd.context.getConf.contains("spark.default.parallelism")) {
        new HashPartitioner(rdd.context.defaultParallelism)
      }else{
        new HashPartitioner(rdds.map(_.partitions.length).max)
      }
    }
  }
}

class HashPartitioner(partitions:Int) extends Partitioner{

  require(partitions>=0,s"Number of partition ${partitions} cannot be negative")

  override def numPartition: Int = partitions

  override def getPartition(key: Any): Int = key match{
    case null => 0
    case _=> Utils.nonNegativeMod(key.hashCode(),numPartition())
  }

  override def hashCode(): Int = partitions

  override def equals(obj: Any): Boolean = obj match{
    case h:HashPartitioner => h.numPartition == numPartition
    case _ => false
  }


}