(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('ProgramDetailController', ProgramDetailController);

    ProgramDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Program', 'ProgramLevel'];

    function ProgramDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Program, ProgramLevel) {
        var vm = this;

        vm.program = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('balancepositionApp:programUpdate', function(event, result) {
            vm.program = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
