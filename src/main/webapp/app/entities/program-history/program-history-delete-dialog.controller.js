(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('ProgramHistoryDeleteController',ProgramHistoryDeleteController);

    ProgramHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProgramHistory'];

    function ProgramHistoryDeleteController($uibModalInstance, entity, ProgramHistory) {
        var vm = this;

        vm.programHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProgramHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
