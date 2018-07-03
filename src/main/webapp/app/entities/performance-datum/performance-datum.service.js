(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('PerformanceDatum', PerformanceDatum);

    PerformanceDatum.$inject = ['$resource', 'DateUtils'];

    function PerformanceDatum ($resource, DateUtils) {
        var resourceUrl =  'api/performance-data/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.timestamp = DateUtils.convertLocalDateFromServer(data.timestamp);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.timestamp = DateUtils.convertLocalDateToServer(copy.timestamp);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.timestamp = DateUtils.convertLocalDateToServer(copy.timestamp);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
