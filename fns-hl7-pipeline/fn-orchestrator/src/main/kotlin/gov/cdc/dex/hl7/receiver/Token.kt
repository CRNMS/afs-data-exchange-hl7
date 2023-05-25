package gov.cdc.dex.hl7.receiver

data class Token(
        @SerializedName("UTF_BOM") val UTF_BOM: String = "\uFEFF",
        @SerializedName("STATUS_SUCCESS") val STATUS_SUCCESS: String = "SUCCESS",
        @SerializedName("STATUS_ERROR") val STATUS_ERROR: String = "ERROR",
        @SerializedName("MSH_9_PATH") val MSH_9_PATH: String = "MSH-9",
        @SerializedName("MSH_21_1_1_PATH") val MSH_21_1_1_PATH: String = "MSH-21[1].1",
        @SerializedName("MSH_21_2_1_PATH") val MSH_21_2_1_PATH: String = "MSH-21[2].1",
        @SerializedName("MSH_21_3_1_PATH") val MSH_21_3_1_PATH: String = "MSH-21[3].1",
        @SerializedName("EVENT_CODE_PATH") val EVENT_CODE_PATH: String = "OBR-31.1",
        @SerializedName("JURISDICTION_CODE_PATH")
        val JURISDICTION_CODE_PATH: String = "OBX[@3.1='77968-6']-5.1",
        @SerializedName("ALT_JURISDICTION_CODE_PATH")
        val ALT_JURISDICTION_CODE_PATH: String = "OBX[@3.1='NOT116']-5.1",
)
