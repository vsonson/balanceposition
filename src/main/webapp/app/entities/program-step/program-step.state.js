(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('program-step', {
            parent: 'entity',
            url: '/program-step?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProgramSteps'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/program-step/program-steps.html',
                    controller: 'ProgramStepController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('program-step-detail', {
            parent: 'program-step',
            url: '/program-step/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProgramStep'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/program-step/program-step-detail.html',
                    controller: 'ProgramStepDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ProgramStep', function($stateParams, ProgramStep) {
                    return ProgramStep.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'program-step',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('program-step-detail.edit', {
            parent: 'program-step-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program-step/program-step-dialog.html',
                    controller: 'ProgramStepDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProgramStep', function(ProgramStep) {
                            return ProgramStep.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('program-step.new', {
            parent: 'program-step',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program-step/program-step-dialog.html',
                    controller: 'ProgramStepDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                descriptionF: null,
                                mediaUrl: null,
                                isLocked: false,
                                isPaid: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('program-step', null, { reload: 'program-step' });
                }, function() {
                    $state.go('program-step');
                });
            }]
        })
        .state('program-step.edit', {
            parent: 'program-step',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program-step/program-step-dialog.html',
                    controller: 'ProgramStepDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProgramStep', function(ProgramStep) {
                            return ProgramStep.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('program-step', null, { reload: 'program-step' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('program-step.delete', {
            parent: 'program-step',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program-step/program-step-delete-dialog.html',
                    controller: 'ProgramStepDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProgramStep', function(ProgramStep) {
                            return ProgramStep.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('program-step', null, { reload: 'program-step' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
