(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('MetricDatum', MetricDatum);

    MetricDatum.$inject = ['$resource', 'DateUtils'];

    function MetricDatum ($resource, DateUtils) {
        var resourceUrl =  'api/metric-data/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.timestamp = DateUtils.convertDateTimeFromServer(data.timestamp);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
