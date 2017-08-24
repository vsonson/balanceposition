(function() {
    'use strict';
    angular
        .module('balancepositionApp')
        .factory('TrackMetricQuestion', TrackMetricQuestion);

    TrackMetricQuestion.$inject = ['$resource'];

    function TrackMetricQuestion ($resource) {
        var resourceUrl =  'api/track-metric-questions/:id';

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
