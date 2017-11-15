(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('UserInfo', UserInfo);

    UserInfo.$inject = ['$resource', 'DateUtils'];

    function UserInfo ($resource, DateUtils) {
        var resourceUrl =  'api/user-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateOfBirth = DateUtils.convertDateTimeFromServer(data.dateOfBirth);
                        data.lastQuoteDate = DateUtils.convertLocalDateFromServer(data.lastQuoteDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.lastQuoteDate = DateUtils.convertLocalDateToServer(copy.lastQuoteDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.lastQuoteDate = DateUtils.convertLocalDateToServer(copy.lastQuoteDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
