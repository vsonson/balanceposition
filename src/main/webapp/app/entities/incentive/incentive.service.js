(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('Incentive', Incentive);

    Incentive.$inject = ['$resource'];

    function Incentive ($resource) {
        var resourceUrl =  'api/incentives/:id';

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
