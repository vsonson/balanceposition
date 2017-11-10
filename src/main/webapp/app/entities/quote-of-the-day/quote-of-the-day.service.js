(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('QuoteOfTheDay', QuoteOfTheDay);

    QuoteOfTheDay.$inject = ['$resource'];

    function QuoteOfTheDay ($resource) {
        var resourceUrl =  'api/quote-of-the-days/:id';

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
