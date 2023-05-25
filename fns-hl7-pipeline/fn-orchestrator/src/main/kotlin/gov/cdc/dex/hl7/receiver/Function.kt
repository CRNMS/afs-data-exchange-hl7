package gov.cdc.dex.hl7.receiver

import com.azure.messaging.eventhubs.*
import com.azure.storage.blob.*
import com.azure.storage.blob.models.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.annotation.BindingName
import com.microsoft.azure.functions.annotation.EventHubTrigger
import com.microsoft.azure.functions.annotation.FunctionName
import gov.cdc.dex.azure.EventHubMetadata
import gov.cdc.dex.azure.EventHubSender
import gov.cdc.dex.azure.RedisProxy
import gov.cdc.dex.metadata.*
import gov.cdc.dex.mmg.InvalidConditionException
import gov.cdc.dex.mmg.MmgUtil
import gov.cdc.dex.util.DateHelper.toIsoString
import gov.cdc.dex.util.StringUtils.Companion.hashMD5
import gov.cdc.dex.util.StringUtils.Companion.normalize
import gov.cdc.hl7.HL7StaticParser
import java.io.*
import java.util.*


/**
 * Azure Functions with Event Hub Trigger.
 */
class Function {
    companion object {
        /*
        const val BLOB_CREATED = "Microsoft.Storage.BlobCreated"
        const val UTF_BOM = "\uFEFF"
        const val STATUS_SUCCESS = "SUCCESS"
        const val STATUS_ERROR = "ERROR"
        const val MSH_21_2_1_PATH = "MSH-21[2].1" // Generic or Arbo
        const val MSH_21_3_1_PATH = "MSH-21[3].1" // Condition
        const val EVENT_CODE_PATH = "OBR-31.1"
        const val JURISDICTION_CODE_PATH = "OBX[@3.1='77968-6']-5.1"
        const val ALT_JURISDICTION_CODE_PATH = "OBX[@3.1='NOT116']-5.1"
        val gson: Gson = GsonBuilder().serializeNulls().create()
        */
    }
    @FunctionName("orchestrator-poc")
    fun eventHubProcessor(
        @EventHubTrigger(
                name = "msg", 
                eventHubName = "%EventHubReceiveName%",
                consumerGroup = "%EventHubConsumerGroup%",
                connection = "EventHubConnectionString") 
                messages: List<String>?,
                @BindingName("SystemPropertiesArray")
                eventHubMD:List<EventHubMetadata>, 
                context: ExecutionContext) {
            /*
            Receive message and pass along to function

            - Function name i
            - 
            -
            -          
            
             */
        // Convert Message Object into HL7 Token

        val startTime = Date().toIsoString()
        // context.logger.info("message: --> " + message)
        val evHubName = System.getenv("EventHubSendOkName")
        val evHubErrsName = System.getenv("EventHubSendErrsName")
        val evHubConnStr = System.getenv("EventHubConnectionString")
        val blobIngestContName = System.getenv("BlobIngestContainerName")
        val ingestBlobConnStr = System.getenv("BlobIngestConnectionString")
        // val redisName: String = System.getenv("REDIS_CACHE_NAME")
        // val redisKey: String = System.getenv("REDIS_CACHE_KEY")
        // val redisProxy = RedisProxy(redisName, redisKey)
        // val mmgUtil = MmgUtil(redisProxy)
        val evHubSender = EventHubSender(evHubConnStr)
        val azBlobProxy = AzureBlobProxy(ingestBlobConnStr, blobIngestContName)

        // Read Message
        val messageInfo = getMessageInfo( message )


        // Retrieve Token


        // Determine Next Step - Function to Pass


        // Send Message to new Function

    } // .eventHubProcess

    private fun getMessageInfo( message : String): HL7Token {
        val structuredMessage = gson.fromJson(
            message, HL7Token::class.java
        )
        // Space for Logic - If needed.
        return structuredMessage
    }

    private fun extractValue(msg: String, path: String): String  {
        val value = HL7StaticParser.getFirstValue(msg, path)
        return if (value.isDefined) value.get()
        else ""
    }


    private fun prepareAndSend(messageContent: ArrayList<String>, messageInfo: DexMessageInfo, 
    metadata: DexMetadata, summary: SummaryInfo, 
    eventHubSender: EventHubSender, eventHubName: String, context: ExecutionContext) {
        val contentBase64 = Base64.getEncoder().encodeToString(messageContent.joinToString("\n").toByteArray())
        val msgEvent = DexEventPayload(contentBase64, messageInfo, metadata, summary)
        context.logger.info("Sending new Event to event hub Message: --> messageUUID: ${msgEvent.messageUUID}, messageIndex: ${msgEvent.metadata.provenance.messageIndex}, fileName: ${msgEvent.metadata.provenance.filePath}")
        val jsonMessage = gson.toJson(msgEvent)
        eventHubSender.send(evHubTopicName=eventHubName, message=jsonMessage)
        context.logger.info("full message: $jsonMessage")
        context.logger.info("Processed and Sent to event hub $eventHubName Message: --> messageUUID: ${msgEvent.messageUUID}, messageIndex: ${msgEvent.metadata.provenance.messageIndex}, fileName: ${msgEvent.metadata.provenance.filePath}")
        //println(msgEvent)
    }


} // .class  Function

