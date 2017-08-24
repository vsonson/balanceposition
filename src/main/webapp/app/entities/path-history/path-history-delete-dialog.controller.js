(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('PathHistoryDeleteController',PathHistoryDeleteController);

    PathHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'PathHistory'];

    function PathHistoryDeleteController($uibModalInstance, entity, PathHistory) {
        var vm = this;

        vm.pathHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PathHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
