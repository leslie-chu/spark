package com.weibo.util

/**
  * Created by zhenfei1 on 17/7/6.
  */
private[weibo] object Utils {

  def nonNegativeMod(x:Int,mod:Int)={
    val rowMod = x%mod;
    rowMod + (if(rowMod<0) mod else 0)
  }
}
