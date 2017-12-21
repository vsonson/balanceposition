(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('DataPoint', DataPoint);

    DataPoint.$inject = ['$resource'];

    function DataPoint ($resource) {
        var resourceUrl =  'api/data-points/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
