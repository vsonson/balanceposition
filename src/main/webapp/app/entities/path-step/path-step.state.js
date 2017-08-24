(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('path-step', {
            parent: 'entity',
            url: '/path-step?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PathSteps'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/path-step/path-steps.html',
                    controller: 'PathStepController',
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
        .state('path-step-detail', {
            parent: 'path-step',
            url: '/path-step/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PathStep'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/path-step/path-step-detail.html',
                    controller: 'PathStepDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PathStep', function($stateParams, PathStep) {
                    return PathStep.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'path-step',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('path-step-detail.edit', {
            parent: 'path-step-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/path-step/path-step-dialog.html',
                    controller: 'PathStepDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PathStep', function(PathStep) {
                            return PathStep.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('path-step.new', {
            parent: 'path-step',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/path-step/path-step-dialog.html',
                    controller: 'PathStepDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                desc: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('path-step', null, { reload: 'path-step' });
                }, function() {
                    $state.go('path-step');
                });
            }]
        })
        .state('path-step.edit', {
            parent: 'path-step',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/path-step/path-step-dialog.html',
                    controller: 'PathStepDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PathStep', function(PathStep) {
                            return PathStep.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('path-step', null, { reload: 'path-step' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('path-step.delete', {
            parent: 'path-step',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/path-step/path-step-delete-dialog.html',
                    controller: 'PathStepDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PathStep', function(PathStep) {
                            return PathStep.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('path-step', null, { reload: 'path-step' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
