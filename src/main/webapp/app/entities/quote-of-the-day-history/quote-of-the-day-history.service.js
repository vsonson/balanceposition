(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('QuoteOfTheDayHistory', QuoteOfTheDayHistory);

    QuoteOfTheDayHistory.$inject = ['$resource'];

    function QuoteOfTheDayHistory ($resource) {
        var resourceUrl =  'api/quote-of-the-day-histories/:id';

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
