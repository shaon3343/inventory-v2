"use strict";

function Application_default(window, document, undefined, options) {

    var formValidatorDefaults = {
        invalidHandler: function (event, validator) { //display error alert on form submit
            $('.alert-danger', this).show();
        },

        highlight: function (element) { // hightlight error inputs
            $(element).closest('.form-group').addClass('has-error'); // set error class to the control group
        },

        success: function (label) {
            label.closest('.form-group').removeClass('has-error');
            label.remove();
        },

        errorPlacement: function (error, element) {
            error.insertAfter(element.closest('.input-icon'));
        },
        submitHandler: function (form) {
            form.submit();
            return;
        }
    };

    options = $.extend(true, {
        formValidator: {
            'update-key-alias-form-validator': {
                errorElement: 'span',
                errorClass: 'help-block',
                focusInvalid: false,
                rules: {
                    alias: {
                        required: true
                    }
                },
                messages: {
                    alias: {
                        required: "<span style='color: #FF0000'>Alias is required.</span>"
                    }
                }
            }
        },
        eventCallback: {
            'key-alias-header-toggle-action': function (target) {
                var context = $(target);
                $(".full", context).hide();
                $(".short", context).show();

                $(".short", context).mouseover(function () {
                    $(".full", context).show();
                    $(".short", context).hide();
                });

                $(".full", context).mouseleave(function () {
                    $(".full", context).hide();
                    $(".short", context).show();
                });
            },
        },
        actionInterceptors: {
            'position-change-event-interceptor': function (target) {
                var self = $(target);
                $("input[name=positionId]", $("#service-search-form")).val(self.val());
                return true;
            },
            'search-request-event-interceptor': function (target) {
                $("#service-list").html('<i class="fa fa-circle-o-notch fa-spin" style="font-size:24px"></i>')
                return true;
            },
            'data-table-get-search-request-options-interceptor': function (target, requestParams) {
                var requiredColumns = [];
                requiredColumns["columns"] = [
                    {
                        "class": "check-box-picker",
                        "orderable": false,
                        "data": null,
                        "defaultContent": "<input type='checkbox' class='checkboxAll'/>"
                    },
                    {"data": "accessRequestId"},
                    {"data": "requestTime"},
                    {"data": "delegateGroupName"},
                    {"data": "delegateName"},
                    {"data": "maxQps"},
                    {"data": "maxQpm"},
                    {"data": "maxConnection"},
                    {"data": "consumerGroup"},
                    {"data": "consumerAccount"},
                    {"data": "status"},
                    {"data": "actionFullName"},
                    {
                        "defaultContent": "<input type='text' id='rejectReason' name='rejectReason' value='' maxlength='250'/>",
                        "orderable": false,
                        "data": null,
                        "class": "reject-reason"
                    },
                    {
                        "class": "lessDetail",
                        "orderable": false,
                        "data": null,
                        "defaultContent": ""
                    },
                    {
                        "data": "authKey",
                        "class": "detail"
                    },
                    {
                        "data": "serviceName",
                        "class": "detail"
                    },
                    {
                        "data": "serviceAliasName",
                        "render": $.fn.dataTable.render.text(),
                        "class": "detail"
                    },
                    {
                        "data": "providerAliasName",
                        "class": "detail"
                    },
                    {
                        "class": "detail",
                        "defaultContent": "",
                        "orderable": false,
                        "data": null
                    }
                ];

                requiredColumns["columnDefs"] = [
                    {
                        'targets': 0,
                        'createdCell': function (td, cellData, rowData, row, col) {
                            $(td).attr('data-request-id', rowData.accessRequestId);
                            $(td).attr('data-action', rowData.action);
                            $(td).attr('data-approver-account', rowData.approverAccount);
                            $(td).attr('data-rejection-reason', rowData.rejectionReason);
                            $(td).attr('data-requester-account', rowData.requesterAccount);
                            $(td).attr('data-requester-email', rowData.requesterEmail);
                            $(td).attr('data-status', rowData.statusValue);
                            var pending = '0';
                            if (rowData.statusValue !== pending) {
                                $(td).find('input').attr("disabled", true);
                                $(td).find('input').removeClass('checkboxAll');
                            }
                        }
                    },
                    {
                        'targets': 1,
                        'createdCell': function (td, cellData, rowData, row, col) {
                            $(td).attr('class', 'access-request-id-class');
                        }
                    },
                    {
                        'targets': 2,
                        'createdCell': function (td, cellData, rowData, row, col) {
                            $(td).attr('class', 'request-time')
                        }
                    },
                    {
                        'targets': 3,
                        'createdCell': function (td, cellData, rowData, row, col) {
                            $(td).attr('class', 'delegate-group-name')
                        }

                    },
                    {
                        'targets': 4,
                        'createdCell': function (td, cellData, rowData, row, col) {
                            $(td).attr('class', 'delegate-name')
                        }

                    },
                    {
                        'targets': 5,
                        'createdCell': function (td, cellData, rowData, row, col) {
                            $(td).attr('class', 'max-qps');
                            if (cellData > 100) {
                                $(td).parent().css('background-color', '#ff5f5d');
                                $(td).parent().attr('title', 'Need Moderator confirmation');
                            }
                        }

                    },
                    {
                        'targets': 6,
                        'createdCell': function (td, cellData, rowData, row, col) {
                            $(td).attr('class', 'max-qpm')
                        }

                    },
                    {
                        'targets': 7,
                        'createdCell': function (td, cellData, rowData, row, col) {
                            $(td).attr('class', 'max-connection')
                        }

                    },
                    {
                        'targets': 8,
                        'createdCell': function (td, cellData, rowData, row, col) {
                            $(td).attr('class', 'consumer-group')
                        }

                    },
                    {
                        "render": function (data, type, row) {
                            var link = "";
                            if (data !== null) {
                                link = "<a href='mailto:" + row.requesterEmail + "?subject=" + row.delegateName
                                    + " &body=This is " + ($(target).data("user-name") !== undefined ? $(target).data("user-name") : "") + " from " + row.providerAliasName + "'>"
                                    + data + "</a>";
                            }
                            return link;
                        },
                        'createdCell': function (td, cellData, rowData, row, col) {
                            $(td).attr('class', 'consumer-account')
                        },
                        "targets": 9
                    },
                    {
                        'targets': 10,
                        'createdCell': function (td, cellData, rowData, row, col) {
                            $(td).attr('class', 'request-status');
                        }
                    },
                    {
                        'targets': 12,
                        'createdCell': function (td, cellData, rowData, row, col) {
                            $(td).find('#rejectReason').val(rowData.rejectionReason);

                        }
                    },
                    {
                        'targets': 14,
                        'createdCell': function (td, cellData, rowData, row, col) {
                            $(td).attr('class', 'detail auth-key');

                        }
                    },
                    {
                        'targets': 15,
                        'createdCell': function (td, cellData, rowData, row, col) {
                            $(td).attr('class', 'detail service-name');

                        }
                    },
                    {
                        'targets': 16,
                        'createdCell': function (td, cellData, rowData, row, col) {
                            $(td).attr('class', 'detail service-alias-name');

                        }
                    },
                    {
                        'targets': 17,
                        'createdCell': function (td, cellData, rowData, row, col) {
                            $(td).attr('class', 'detail provider-alias-name');

                        }
                    }
                ];

                var dataSourceUrl = $(target).data("ajax-url");
                var requestMethod = $(target).data("request-method");
                var errorUrl = $(target).data("base-error-url");
                var isUpdateRequest = $(target).data("is-update-request");
                var options = {
                    "ajax": {
                        "url": dataSourceUrl,
                        "type": requestMethod,
                        data: function () {
                            return JSON.stringify(requestParams);
                        },
                        dataType: "json",
                        processData: false,
                        contentType: "application/json;charset=UTF-8",
                        complete: function (xhr) {
                            if (isUpdateRequest === true && (xhr.status >= 200 || xhr.status < 300)) {
                                if (xhr.status === 207) {
                                    $('.partial-updated').show();
                                    $('.partial-updated').fadeOut(5000);
                                }
                                else if (xhr.status === 200) {
                                    $('.all-updated').show();
                                    $('.all-updated').fadeOut(5000);
                                }
                            }
                            else if (isUpdateRequest === true && (xhr.status < 200 || xhr.status >= 300)) {
                                $('#accessRequestList').DataTable()
                                    .clear()
                                    .draw();
                            }
                            $(".detail").hide();
                            if ($('.search-detail:visible').length > 0) {
                                $(".search-detail").show();
                            }
                            $(".lessDetail").show();
                        },
                        "error": function (xhr, error, thrown) {
                            if (error === 'timeout') {
                                alert('The server took too long to send the data.');
                            }
                            else {
                                alert('An error occurred on the server. Please try again in a minute.');
                            }
                            $('#accessRequestList').DataTable()
                                .clear()
                                .draw();
                        }

                    },
                    "columns": requiredColumns["columns"],
                    "columnDefs": requiredColumns["columnDefs"]

                };

                return options;
            }
        },
        eventAction: {
            'reset-search-form-action': function (target) {
                $(target).parent().find('input[type=text], select').val('');
                $(target).parent().find('.datepicker').datepicker('option', 'minDate', null).datepicker('option', 'maxDate', null);
                $("#delegateNames").prop('disabled', 'disabled').empty();
            },
            'search-request-event-action': function (target) {
                var interceptor = $(target).data("action-interceptor");
                var dataTableOptions = {};
                var requestParams = getSearchFormParams("#request-search-form");
                if (interceptor !== undefined) {
                    try {
                        dataTableOptions = options.actionInterceptors[interceptor](target, requestParams);
                    } catch (ex) {

                    }
                }
                $('#accessRequestList').DataTable().clear();
                $('#accessRequestList').DataTable().destroy();
                $("#accessRequestList").DataTable(dataTableOptions);
                $("#accessRequestList").initializeFeatures();
            },
            'update-request-event-action': function (target) {
                var form = $(target).closest("form");
                var selectedRequests = [];
                $(".checkboxAll:checkbox:checked", form).each(function () {
                    var request = prepareAccessRequestUpdateParams(this);
                    selectedRequests.push(request);
                });
                if ($(selectedRequests).length > 0) {
                    var requestParams = {
                        "searchForm": getSearchFormParams("#request-search-form"),
                        "accessRequests": selectedRequests
                    };
                    var interceptor = $(target).data("action-interceptor");
                    var dataTableOptions = {};
                    if (interceptor !== undefined) {
                        try {
                            dataTableOptions = options.actionInterceptors[interceptor](target, requestParams);
                        } catch (ex) {
                            console.log("Exception in getting data table options")
                        }
                    }
                    $('#accessRequestList').DataTable().clear();
                    $('#accessRequestList').DataTable().destroy();
                    $("#accessRequestList").DataTable(dataTableOptions);
                    $("#accessRequestList").initializeFeatures();

                }
                else {
                    alert("Please select atleast one request!")
                }

            }
        },
        jsonResponseHandlers: {},
        autocompleteProvider: {
            'delegates-autocomplete-datasource-param-provider': function (target, params) {
                return {
                    term: params.term,
                    delegateGroupId: $("#delegateGroup").val(),
                };
            },
            'delegates-autocomplete-datasource-filter': function (target, data) {
                var delegates = [];
                for (var x = 0; x < data.length; x++) {
                    delegates.push({
                        id: data[x].delegateId,
                        text: data[x].delegateName
                    });
                }
                return {results: JSON.parse(JSON.stringify(delegates))};
            }
        },
        featureInitializations: {
            'auto-play-video': function (target) {
                var vid = target.find(".auto-play-video");
                var video = $(vid).find("#video");
                 /*
                    $(video).autoplay = true;
                    $(video).load();
                */
                $(video)[0].play();
            },
            'add-test-case': function (target) {
                var add = target.find(".add-test-case");
                $(add).click(function () {
                    location.reload();
                });
            },
            'smart-json-form': function (container) {
                container.find('.smart-json-form').submit(function () {
                    sendSmartJsonForm($(this));
                    return false;
                });
            },
            'show-response-by-status': function (target) {
                target.find('.show-response-by-status').bind('change', function (e) {
                    $(this).parents().find("div.response-div div[id^='response-header']").hide();
                    $(this).parents().find("div.response-div table[id^='response-body']").hide();
                    $(this).parents().find("div.response-div table[id^='response-assertion']").hide();
                    $(this).parents().find("div.response-div textarea[id^='xml-view']").hide();

                    var trigger = $(this);
                    var responseStatus = $("option:selected", trigger).data("response-status");
                    var responseHeaderId = "response-header" + responseStatus;
                    var responseBodyId = "response-body" + responseStatus;
                    var assertionBodyId = "response-assertion-" + responseStatus;
                    var xmlView = "xml-view-" + responseStatus;

                    $(this).parents().find("div.response-div div[id='" + responseHeaderId + "']").show();
                    $(this).parents().find("div.response-div table[id='" + responseBodyId + "']").show();
                    $(this).parents().find("div.response-div table[id='" + assertionBodyId + "']").show();
                    $(this).parents().find("div.response-div textarea[id='" + xmlView + "']").show();
                });
            },
            'smart-tab': function (container) {
                var tab = container.find(".nav-tabs a");
                $(tab).click(function () {
                    $(this).tab('show');
                });
            },
            'remove-row': function (container) {
                var removeIcon = container.find(".remove-row");
                $(removeIcon).click(function () {
                    $(this).parent().parent().remove();
                });

            },
            'add-row': function (target) {
                var addRow = target.find(".add-row");
                $(addRow).click(function () {
                    var placeHolder = $(this).parent().find("table tbody");
                    placeHolder.append("<tr>" +
                        "<td style='width: 40%'><input type='text' class='form-control' style='display:inherit;width: 40%;'></td>" +
                        "<td><input type='text' class='form-control' style='display:inherit;width: 60%;'></td>" +
                        "<td><span class='glyphicon glyphicon-minus remove-row' data-features='remove-row'></span>" +
                        "</td>" +
                        "</tr>");
                    $(placeHolder).initializeFeatures();
                });

            },
            'add-pre-condition': function (target) {
                var addPreCond = target.find(".add-pre-condition");
                $(addPreCond).click(function () {
                    var placeHolder = $(this).parent().find("table tbody");
                    placeHolder.append("<tr>" +
                        "<td>" +
                        "<input type='text' class='form-control' style='display:inherit;width: 80%;'>" +
                        "</td>" +
                        "<td>" +
                        "<span class='glyphicon glyphicon-minus remove-row' data-features='remove-row'></span>" +
                        "</td>" +
                        "</tr>");
                    $(placeHolder).initializeFeatures();
                });

            },
            'add-child': function (target) {
                var addChild = target.find(".add-child");
                $(addChild).click(function () {
                    var tag = $(this).data("key");
                    var clzName = tag.replace(/\./g, '_');
                    var target = $("." + clzName);
                    var groupPrefix = $(this).data("group-prefix");
                    target.last().parent("tr").after("<tr>" +
                        "<td class=" + clzName + ">" +
                        tag + "[" + target.length + "]" +
                        "</td>" +
                        "<td>" +
                        "<input type='text' name='" + groupPrefix + "_" + tag + target.length + "' class='form-control' style='display:inherit;width: 60%;'>" +
                        "<span class='glyphicon glyphicon-minus remove-row' data-key='" + tag + "' data-features='remove-row'>" +
                        "</td>" +
                        "</tr>");

                    $("." + clzName).last().parent("tr").initializeFeatures();
                });
            },
            'smart-tree-toggle': function (container) {
                var treeToggle = container.find(".tree-toggle");
                $(treeToggle).click(function () {
                    $(this).parent().children("ul.tree").toggle(200);
                });
                $(function () {
                    $(treeToggle).parent().children("ul.tree").toggle(200);
                })
            },
            'load-file': function (container) {
                var fileInfo = container.find(".load-file");
                $(fileInfo).click(function () {
                    var fileData = $(this).data("file-location");
                    $("#data-container").text(fileData).html();
                });

            },
            'load-result': function (container) {
                var resultInfo = container.find(".load-result");
                $(resultInfo).click(function () {
                    var resultData = $(this).data("result");
                    var statusData = $(this).data("status");
                    $("#result-container").text(resultData).html();
                    $("#status-container").text(JSON.stringify(statusData, null, 4)).html();
                });
            },
            'load-evidence': function (container) {
                var evidenceInfo = container.find(".load-evidence");
                $(evidenceInfo).click(function () {
                    $("#diff-container").empty();
                    $("#raw-request-container").empty();
                    $("#raw-response-container").empty();
                    $("#result-container").empty();

                    var resultDiffData = $(this).data("result-diff");
                    var rawResponseData = $(this).data("raw-response");
                    var rawRequestData = $(this).data("raw-request");
                    var resultData = $(this).data("result");

                    $('#diff-container').html(resultDiffData);
                    $('#raw-request-container').text(rawRequestData).html();
                    $('#raw-response-container').text(rawResponseData).html();
                    $("#result-container").text('').html(resultData);
                });
            },
            'smart-toggle': function (container) {
                var delegateGroups = container.find(".smart-toggle");
                if (delegateGroups.value == "" || delegateGroups.value == null) {
                    $("#delegateNames").prop('disabled', 'disabled').empty();
                }
                delegateGroups.bind('change', function () {
                    if (this.value == "" || this.value == null) {
                        $("#delegateNames").prop('disabled', 'disabled').empty();
                    }
                    else {
                        $("#delegateNames").empty().removeAttr("disabled");
                    }
                })
            },
            'smart-autocomplete': function (container) {
                container.find(".autocomplete").each(function () {
                    var self = $(this);
                    var isRemoteDataSource = ($(self).attr('data-source-remote') != undefined);
                    var placeHolderText = $(self).attr('data-placeholder-text');

                    if (placeHolderText == undefined || placeHolderText.length <= 0) {
                        placeHolderText = "Select..."
                    }

                    if (isRemoteDataSource) {
                        var dataSourceUrl = $(self).attr('data-source-url');
                        var dataSourceParamProviderKey = $(self).attr('data-source-param-provider');
                        var dataSourceFilterProviderKey = $(self).attr('data-source-filter-provider');

                        var ajaxCall = {
                            url: dataSourceUrl,
                            dataType: 'json',
                            type: "GET",
                            delay: 250,
                            data: function (params) {
                                var autoCompleteProvider = dataSourceParamProviderKey === undefined ? undefined : options.autocompleteProvider[dataSourceParamProviderKey];
                                if (autoCompleteProvider !== undefined) {
                                    try {
                                        return autoCompleteProvider(self, params);
                                    } catch (ex) {
                                    }
                                }
                                return [];
                            },
                            processResults: function (data) {
                                var dataSourceFilterProvider = dataSourceFilterProviderKey === undefined ? undefined : options.autocompleteProvider[dataSourceFilterProviderKey];
                                if (dataSourceFilterProvider !== undefined) {
                                    try {
                                        return dataSourceFilterProvider(self, data);
                                    } catch (ex) {
                                    }
                                }
                                return [];
                            },
                            cache: true
                        }

                        $(self).select2({
                            placeholder: placeHolderText,
                            minimumInputLength: 1,
                            ajax: ajaxCall
                        });

                    }
                })
            },
            'smart-check-box-picker': function (container) {
                container.find('.check-box-picker').each(function () {
                    var self = $(this);
                    var groupClass = self.data("choice-group-class");
                    var ctx = $(self).closest(self.data("context-type"))

                    self.click(function () {
                        if ($(self).is(':checked')) {
                            $("." + groupClass, $(ctx)).prop('checked', true);
                        } else {
                            $("." + groupClass, $(ctx)).prop('checked', false);
                        }
                    });

                });
            },
            'more-detail-show': function (container) {
                container.find('#more-detail-button').each(function () {
                    var self = $(this);
                    $(this).on("click", function (event) {
                        $(".detail").show();
                        $(".search-detail").show();
                        $(".lessDetail").hide();
                        var i = 0;
                        $('#accessRequestList tbody tr td').each(function () {
                            i++;
                        });
                        if (i === 1)
                            $("#accessRequestList tbody tr td").attr("colspan", 17);
                    });
                });
            },
            'less-detail-show': function (container) {
                container.find('#less-detail-button').each(function () {
                    var self = $(this);
                    $(this).on("click", function (event) {
                        $(".detail").hide();
                        $(".search-detail").hide();
                        $(".lessDetail").show();
                    });
                });
            },
            'smart-data-table': function (container) {
                container.find("[data-features='smart-data-table']").each(function () {
                    var interceptor = $(this).data("action-interceptor");
                    var dataTableOptions = {};
                    var requestParams = getSearchFormParams("#request-search-form");
                    if (interceptor !== undefined) {
                        try {
                            dataTableOptions = options.actionInterceptors[interceptor](this, requestParams);
                        } catch (ex) {

                        }
                    }
                    $(this).DataTable(
                        dataTableOptions
                    );

                    $(this).on('page.dt', function () {
                        $(".detail").hide();
                        $(".lessDetail").show();
                    });

                    $(this).on('order.dt', function () {
                        $(".detail").hide();
                        $(".lessDetail").show();
                    });
                })
            },
            'smart-button': function (container) {
                container.find(".button").each(function () {
                    $(this).on("click", function (event) {
                        var trigger = $(this);
                        var eventKey = trigger.attr('data-action-event');

                        var eventAction = eventKey === undefined ? undefined : options.eventAction[eventKey];
                        if (eventAction !== undefined) {
                            try {
                                eventAction(trigger);
                            } catch (ex) {
                            }
                        }
                    })
                })
            },
            'smart-from-date': function (container) {
                container.find('.smart-from-date').each(function () {
                    $(this).datepicker({
                        onSelect: function (selectedDate) {
                            $("#" + $(this).data("to-date-field")).datepicker('option', 'minDate', selectedDate);
                        },
                        dateFormat: 'yy/mm/dd',
                        changeYear: true,
                        changeMonth: true
                    });
                });
            },
            'smart-to-date': function (container) {
                container.find('.smart-to-date').each(function () {
                    $(this).datepicker({
                        onSelect: function (selectedDate) {
                            $("#" + $(this).data("from-date-field")).datepicker('option', 'maxDate', selectedDate);
                        },
                        dateFormat: 'yy/mm/dd',
                        changeYear: true,
                        changeMonth: true
                    });
                });
            },

            'smart-dialog-box': function (container) {
                container.find("[data-features='smart-dialog-box']").each(function () {
                    //notification dialog
                    $("#open-notification", this).click(function () {

                        $("#dialog-notification").dialog({
                            height: 400,
                            width: 600,
                            close: function (event, ui) {
                                var actionUrl = $(this).data("ajax-url");
                                var requestMethod = $(this).data("request-method");

                                $.ajax({
                                    url: actionUrl,
                                    type: requestMethod,
                                    success: function (html) {
                                        $("#notificationDiv").html(html);
                                        $("#notificationDiv").initializeFeatures();
                                    },
                                    error: function (request, status, error) {
                                        alert("Opps! Something went wrong!");
                                    }
                                });
                            }
                        });

                    });

                })
            }

        }
    });

    function sendSmartJsonForm(form) {
        //  alert(form.serialize())
        $.ajax({
            type: form.attr('method').toUpperCase(),
            url: getActionUrl(form),
            data: form.serialize(),
            success: function (json) {
                var data = json.data;

                var customHandler = form.attr('data-json-handler-' + json.type);
                if (customHandler !== undefined) {
                    try {
                        options.jsonResponseHandlers[customHandler](form, json);
                    } catch (ex) {
                        throw ex;
                        alert("Something went wrong!")
                    }
                    return;
                }
            },
            error: function (request, textStatus) {
                alert("Something went wrong!");
            }
        });

        return false;
    }

    function getActionUrl(target) {
        var actionUrl = target.attr('data-action-url');

        if (!actionUrl) {
            switch (target.get(0).tagName.toLowerCase()) {
                case 'form':
                    actionUrl = target.attr('action');
                    if (actionUrl) {
                        break;
                    }

                    actionUrl = window.location.href;
                    break;
                case 'a':
                    actionUrl = target.attr('href');
                    break;
                default:
                    break;
            }
        }

        return actionUrl.charAt(0) === '#' ? actionUrl.substring(1) : actionUrl;
    }

    function getSearchFormParams(formId) {
        var formValues = {};
        var allFormData = $(formId).serializeArray();
        $.each(allFormData, function () {
            if (formValues[this.name] !== undefined) {
                if (!formValues[this.name].push) {
                    formValues[this.name] = [formValues[this.name]];
                }
                if (this.value != null && this.value != '')
                    formValues[this.name].push(this.value || '');
            } else {
                if (this.value != null && this.value != '')
                    formValues[this.name] = this.value || '';
            }
        });
        return formValues;
    }

    function prepareAccessRequestUpdateParams(element) {
        var requestId = $(element).closest('td').data('request-id');
        var action = $(element).closest('td').data('action');
        var approverAccount = $(element).closest('td').data('approver-account');
        var rejectReason = $(element).closest('tr').find("#rejectReason").val();
        var requesterEmail = $(element).closest('td').data('requester-email');
        var status = $(element).closest('td').data('status');

        var request = {
            accessRequestId: (requestId === undefined || requestId == null) ? "" : requestId,
            rejectionReason: (rejectReason === undefined || rejectReason == null) ? "" : rejectReason,
            approverAccount: (approverAccount === undefined || approverAccount == null) ? "" : approverAccount,
            requesterEmail: (requesterEmail === undefined || requesterEmail == null) ? "" : requesterEmail,
            status: (status === undefined || status == null) ? "" : status,
            action: (action === undefined || action == null) ? "" : action
        };
        return request;
    }

    function unique(arr) {
        var hash = {}, result = [];
        for (var i = 0, l = arr.length; i < l; ++i) {
            if (!hash.hasOwnProperty(arr[i])) {
                hash[arr[i]] = true;
                result.push(arr[i]);
            }
        }
        return result;
    }

    function createCORSRequest(method, url) {
        var xhr = new XMLHttpRequest();
        if ("withCredentials" in xhr) {

            // Check if the XMLHttpRequest object has a "withCredentials" property.
            // "withCredentials" only exists on XMLHTTPRequest2 objects.
            xhr.open(method, url, true);

        } else if (typeof XDomainRequest != "undefined") {

            // Otherwise, check if XDomainRequest.
            // XDomainRequest only exists in IE, and is IE's way of making CORS requests.
            xhr = new XDomainRequest();
            xhr.open(method, url);

        } else {

            // Otherwise, CORS is not supported by the browser.
            xhr = null;

        }
        return xhr;
    }

    $.fn.initializeFeatures = function () {
        var containerFeatures = this.attr('data-features');
        var allFeatures = containerFeatures === undefined ? '' : containerFeatures;

        this.find('[data-features]').each(function () {
            allFeatures += ' ' + $(this).attr('data-features');
        });

        allFeatures = $.trim(allFeatures + ' alert-message').replace(/\s{2,}/g, ' ');//alert-message and json-link are default features

        var featureArray = unique(allFeatures.split(' '));

        for (var i = 0, len = featureArray.length; i < len; ++i) {
            var feature = featureArray[i];
            try {
                options.featureInitializations[feature](this);
            } catch (ex) {
                if (typeof(isDevEnv) !== 'undefined') {
                    alert(feature + ': ' + ex);
                }
            }
        }

        return this;
    };

    this.initialize = function () {
        $(function () {
            $.ajaxSetup({
                beforeSend: function (jqXHR, settings) {
                    if (typeof settings.error === 'function') {
                        var errorfunc = settings.error;
                        settings.error = function (jqXHR2, textStatus, errorThrown) {
                            if (jqXHR2.status !== 901) {
                                errorfunc(jqXHR2, textStatus, errorThrown);
                            }
                        };
                    }
                },
                statusCode: {
                    901: function () {
                        window.location.reload()
                    }
                }
            });
            $(document.documentElement).initializeFeatures();
        });
    };
}


(function (window, document, undefined) {
    var controller = new Application_default(window, document, undefined);
    controller.initialize();
})(this, this.document);
