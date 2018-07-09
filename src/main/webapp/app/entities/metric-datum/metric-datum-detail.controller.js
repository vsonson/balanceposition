(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('MetricDatumDetailController', MetricDatumDetailController);

    MetricDatumDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MetricDatum', 'DataPoint', 'User'];

    function MetricDatumDetailController($scope, $rootScope, $stateParams, previousState, entity, MetricDatum, DataPoint, User) {
        var vm = this;

        vm.metricDatum = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('balancepositionApp:metricDatumUpdate', function(event, result) {
            vm.metricDatum = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
