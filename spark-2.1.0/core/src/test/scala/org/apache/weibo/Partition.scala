
package org.apache.weibo


trait Partition  extends Serializable{

  def index: Int

  override def hashCode(): Int = super.hashCode()

  override def equals(obj: Any): Boolean = super.equals(obj)
}
