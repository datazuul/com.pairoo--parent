var options = {
    return_type: 'object',
    callback_function_name: 'processPayoneResponse'
}
function processPayoneResponse(response) {
    var status = response.get('status');
    if (status === 'VALID') {
        document.getElementById("cardpan").value = '';
        document.getElementById("cardcvc2").value = '';
        var pseudocardpan = response.get('pseudocardpan');
        var truncatedcardpan = response.get('truncatedcardpan');
        sendResponseToWicket(status, pseudocardpan, truncatedcardpan, null, null, null);
    } else {
        var customermessage = response.get('customermessage');
        var errorcode = response.get('errorcode');
        var errormessage = response.get('errormessage');
        sendResponseToWicket(status, null, null, customermessage, errorcode, errormessage);
    }

}

function sendResponseToWicket(status, pseudocardpan, truncatedcardpan, customermessage, errorcode, errormessage) {
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
        storecarddata: '${storeCardData}',
        cardholder: document.getElementById("${holderNameFieldId}").value,
        cardpan: document.getElementById("cardpan").value,
        cardtype: '${cardType}',
        cardcvc2: document.getElementById("cardcvc2").value,
        language: '${language}'
    }
    var selectYear = document.getElementById("${yearFieldId}");
    var cardexpireyear = selectYear[selectYear.selectedIndex].text;
    var selectMonth = document.getElementById("${monthFieldId}");
    var cardexpiremonth = selectMonth[selectMonth.selectedIndex].text;
    data.cardexpiredate = cardexpireyear + cardexpiremonth;

    var request = new PayoneRequest(data, options);
    request.checkAndStore();
}
