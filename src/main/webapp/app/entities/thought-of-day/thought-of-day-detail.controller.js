(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('ThoughtOfDayDetailController', ThoughtOfDayDetailController);

    ThoughtOfDayDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'ThoughtOfDay'];

    function ThoughtOfDayDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, ThoughtOfDay) {
        var vm = this;

        vm.thoughtOfDay = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('balancepositionApp:thoughtOfDayUpdate', function(event, result) {
            vm.thoughtOfDay = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
