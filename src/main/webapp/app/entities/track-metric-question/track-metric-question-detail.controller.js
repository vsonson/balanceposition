(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('TrackMetricQuestionDetailController', TrackMetricQuestionDetailController);

    TrackMetricQuestionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TrackMetricQuestion', 'KeyPair', 'TrackMetric'];

    function TrackMetricQuestionDetailController($scope, $rootScope, $stateParams, previousState, entity, TrackMetricQuestion, KeyPair, TrackMetric) {
        var vm = this;

        vm.trackMetricQuestion = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:trackMetricQuestionUpdate', function(event, result) {
            vm.trackMetricQuestion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
