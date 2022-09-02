package com.knightboost.apm.blockcanary

interface BlockDetectListener {
    fun onBlockDetected(blockInfo: BlockInfo)
}
