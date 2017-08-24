(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('TriggerDeleteController',TriggerDeleteController);

    TriggerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Trigger'];

    function TriggerDeleteController($uibModalInstance, entity, Trigger) {
        var vm = this;

        vm.trigger = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Trigger.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
