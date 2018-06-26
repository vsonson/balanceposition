(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('StressDatum', StressDatum);

    StressDatum.$inject = ['$resource', 'DateUtils'];

    function StressDatum ($resource, DateUtils) {
        var resourceUrl =  'api/stress-data/:id';

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
