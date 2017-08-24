(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('ProgramLevelDeleteController',ProgramLevelDeleteController);

    ProgramLevelDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProgramLevel'];

    function ProgramLevelDeleteController($uibModalInstance, entity, ProgramLevel) {
        var vm = this;

        vm.programLevel = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProgramLevel.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
