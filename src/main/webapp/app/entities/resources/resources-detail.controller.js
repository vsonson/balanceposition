(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('ResourcesDetailController', ResourcesDetailController);

    ResourcesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Resources'];

    function ResourcesDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Resources) {
        var vm = this;

        vm.resources = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('balancepositionApp:resourcesUpdate', function(event, result) {
            vm.resources = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
