(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('IncentiveAction', IncentiveAction);

    IncentiveAction.$inject = ['$resource'];

    function IncentiveAction ($resource) {
        var resourceUrl =  'api/incentive-actions/:id';

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
