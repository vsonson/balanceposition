(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('ThoughtOfDayDeleteController',ThoughtOfDayDeleteController);

    ThoughtOfDayDeleteController.$inject = ['$uibModalInstance', 'entity', 'ThoughtOfDay'];

    function ThoughtOfDayDeleteController($uibModalInstance, entity, ThoughtOfDay) {
        var vm = this;

        vm.thoughtOfDay = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ThoughtOfDay.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
