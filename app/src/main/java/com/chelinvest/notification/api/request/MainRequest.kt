package com.chelinvest.notification.api.request

import org.simpleframework.xml.*
import com.chelinvest.notification.api.request.SetDeliveryAddressRequest
import com.chelinvest.notification.api.request.UpdateDeliverySubscriptionForBranchRequest

// Документация по SimpleXml
// http://simple.sourceforge.net/download/stream/doc/javadoc/


@Root(name = "tns:xgate_request")
@NamespaceList(
    Namespace(prefix = "tns", reference = "http://www.chelinvest.ru/xgate"),
    Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    Namespace(prefix = "schemaLocation", reference = "http://www.chelinvest.ru/xgate xgate_request.xsd")
)
class MainRequest(element: IRequestElement) {

    @field:Element(name = "datasource")
    var dataSource: String = "INV_PLAT"

    @field:Element(name = "maxrows")
    var maxRows: Long = 1000L

    @field:ElementUnion(
        Element(name = "login_pass", type = LoginRequest::class),
        Element(name = "get_delivery_branch", type = GetDeliveryBranchRequest::class),
        Element(name = "get_delivery_subscription_for_branch", type = GetDeliverySubscriptionForBranchRequest::class),
        Element(name = "get_delivery_types_for_branch", type = GetDeliveryTypesForBranchRequest::class),
        Element(name = "get_delivery_types_for_subscription", type = GetDeliveryTypesForSubscriptionRequest::class),
        Element(name = "set_delivery_address_for_subscription", type = SetDeliveryAddressRequest::class),
        Element(name = "get_input_fields_for_branch", type = GetInputFieldsForBranchRequest::class),
        Element(name = "get_field_values_for_field", type = GetFieldValuesForFieldRequest::class),
        Element(name = "create_delivery_subscription_for_branch", type = CreateDeliverySubscriptionForBranchRequest::class),
        Element(name = "update_delivery_subscription_for_branch", type = UpdateDeliverySubscriptionForBranchRequest::class),
        Element(name = "delete_delivery_subscription_for_branch", type = DeleteDeliverySubscriptionForBranchRequest::class),
        Element(name = "check_agent_limit", type = CheckAgentLimitRequest::class),
        Element(name = "get_agent_info", type = GetAgentInfoRequest::class)
    )

    var element: IRequestElement? = element
}