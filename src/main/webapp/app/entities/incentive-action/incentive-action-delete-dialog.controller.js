(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('IncentiveActionDeleteController',IncentiveActionDeleteController);

    IncentiveActionDeleteController.$inject = ['$uibModalInstance', 'entity', 'IncentiveAction'];

    function IncentiveActionDeleteController($uibModalInstance, entity, IncentiveAction) {
        var vm = this;

        vm.incentiveAction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            IncentiveAction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
