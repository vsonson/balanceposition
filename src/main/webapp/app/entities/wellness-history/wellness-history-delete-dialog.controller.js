(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('WellnessHistoryDeleteController',WellnessHistoryDeleteController);

    WellnessHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'WellnessHistory'];

    function WellnessHistoryDeleteController($uibModalInstance, entity, WellnessHistory) {
        var vm = this;

        vm.wellnessHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WellnessHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
