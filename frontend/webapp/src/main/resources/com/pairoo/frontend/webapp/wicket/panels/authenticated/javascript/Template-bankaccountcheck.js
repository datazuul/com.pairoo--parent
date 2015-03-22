var options = {
    return_type: 'object',
    callback_function_name: 'processPayoneResponse'
}
function processPayoneResponse(response) {
    var status = response.get('status');
    var body = "status=" + Wicket.Form.encode(status);
    if (status == 'VALID') {
        // no other values than "status" contained in response
        sendResponseToWicket(status, null, null, null);
    } else {
        var customermessage = response.get('customermessage');
        var errorcode = response.get('errorcode');
        var errormessage = response.get('errormessage');
        sendResponseToWicket(status, customermessage, errorcode, errormessage);
    }
}

function sendResponseToWicket(status, customermessage, errorcode, errormessage) {
    ${callbackFunctionBody}
}

function submitRequest() {
    var data = {
        encoding: '${encoding}',
        hash: '${hash}',
        mid: '${merchantId}',
        mode: '${mode}',
        portalid: '${portalId}',
        request: '${request}',
        responsetype: '${responseType}',
        aid: '${subaccountId}',
        checktype: '${checkType}',
        bankaccount: document.getElementById("${bankAccountFieldId}").value,
        bankcode: document.getElementById("${bankCodeFieldId}").value,
        bankcountry: '${bankCountryValue}',
        language: '${language}'
    }

    var request = new PayoneRequest(data, options);
    request.checkAndStore();
}
