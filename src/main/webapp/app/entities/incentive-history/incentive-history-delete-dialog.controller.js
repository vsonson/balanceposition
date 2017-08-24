(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('IncentiveHistoryDeleteController',IncentiveHistoryDeleteController);

    IncentiveHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'IncentiveHistory'];

    function IncentiveHistoryDeleteController($uibModalInstance, entity, IncentiveHistory) {
        var vm = this;

        vm.incentiveHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            IncentiveHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
